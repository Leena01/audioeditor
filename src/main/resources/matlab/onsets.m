function locs = onsets(x, fs, bpm, s, lower, smallest)
%ONSETDET Returns the onset of musical notes.
%	x: samples
%	fs: sampling rate
%	bpm: BPM
%	s: filter size
%   lower: lower value of the time signature (value of beat)
%   smallest: smallest note value that might appear in the song
%
%	Author: Lívia Qian

	figure('visible', 'off');
	
	% Observed frequency domain: 20 - 4096 Hz
    bandlimits = 20;
    a = filterbank(x, bandlimits);
    b = hwindow(a, 0.4, bandlimits);
    c = diffrect(b, 1);
    
	% Find peaks (the minimum distance between peaks is determined by the BPM)
    mindis = calcMinDistance(bpm, fs, lower, smallest);
    [pks, locs] = findpeaks(c);

	% Convolution
	s = double(s);
    vec = ones(1, s)/s;
    for i = 1:2
        pks = conv(pks, vec, 'same');
    end
    [pks, locs] = findpeaks(pks, locs, 'MinPeakDistance', mindis);
    
    % Determine the max and removes every peak that's smaller than a threshold
    pivot = max(pks);
    I = pks >= 0.1 * pivot;
    locs = locs(I);
end