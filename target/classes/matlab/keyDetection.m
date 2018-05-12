function [note, scale] = keyDetection(x1, fs1, bpm, s, lower, smallest)
	notes{1} = {'Ab6', 'G6', 'Gb6', 'F6', 'E6', 'Eb6', 'D6', 'Db6', 'C6', 'B5', 'Bb5', 'A5'};
    notes{2} = {'Ab5', 'G5', 'Gb5', 'F5', 'E5', 'Eb5', 'D5', 'Db5', 'C5', 'B4', 'Bb4', 'A4'};
    notes{3} = {'Ab4', 'G4', 'Gb4', 'F4', 'E4', 'Eb4', 'D4', 'Db4', 'C4', 'B3', 'Bb3', 'A3'};
    notes{4} = {'Ab3', 'G3', 'Gb3', 'F3', 'E3', 'Eb3', 'D3', 'Db3', 'C3', 'B2', 'Bb2', 'A2'};
    path = {'Piano.ff.', '.mp3'};
	
    x1 = x1(:, 1);
	
    wlen = 2^10;
    nfft = wlen;
    noverlap = wlen/4;
	
    [S1, ~, ~] = spectrogram(x1, hann(wlen), noverlap, nfft, fs1, 'yaxis');
    [~, slen] = size(S1);
    locs1 = onsets(x1, fs1, bpm, s, lower, smallest);
    locs1 = int32(locs1 * (slen/length(x1)));
	[~, llen] = size(locs1);
	
	n = 12;
	m = 4;

	for j = 1:m
        for i = 1:n
            p = strjoin(path, notes{j}(i));
            sim = noteDetection(S1, locs1, p, wlen, nfft, noverlap);
            sims(i, j, :) = sim;
        end
    end
	
	first = 1;
    res = zeros(first, m, llen);
    for i = 1:llen
        res(:, :, i) = minkcol(sims(:, :, i), first);
    end
	
    modes = zeros(n, 2);
    modes(:, 1) = 1:12;
    for i = 1:llen
        tmp = res(:, :, i);
        tmp = tmp(:);
        tmp = tabulate(tmp);
        [len, ~] = size(tmp);
        for j = 1:len
            modes(tmp(j, 1), 2) = modes(tmp(j, 1), 2) + tmp(j, 2);
        end
    end
	
	modes = flipud(modes);
	
	[note, scale] = getKey(modes);
end