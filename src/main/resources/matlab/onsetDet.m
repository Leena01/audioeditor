function onsetDet(x, fs, bpm, s, lower, smallest, imgname)
%ONSETDET Finds the onset of musical notes.
%	x: samples
%	fs: sampling rate
%	bpm: BPM
%	s: filter size
%   lower: lower value of the time signature (value of beat)
%   smallest: smallest note value that might appear in the song
%	imgname: name of the file to save to
%
%	Author: Lívia Qian

	% Observed frequency domain: 20 - 4096 Hz
    bandlimits = 20;
    a = filterbank(x, bandlimits);
    b = hwindow(a, 0.4, bandlimits);
    c = diffrect(b, 1);
    
	% Find peaks (the minimum distance between peaks is determined by the BPM)
    mindis = calcMinDistance(bpm, fs, lower, smallest);
    [pks, locs] = findpeaks(c);

	% Convolution
    vec = ones(1, s)/s;
    for i = 1:2
        pks = conv(pks, vec, 'same');
    end
    [pks, locs] = findpeaks(pks, locs, 'MinPeakDistance', mindis);
    
    % Determine the max and removes every peak that's smaller than a threshold
    pivot = max(pks);
    I = pks >= 0.1 * pivot;
    locs = locs(I);
    
    % Plot
    plot(x);
    if ~isempty(locs)
        hold on
        line([locs locs], ylim, 'Color', [1 0 0])
        hold off
    end
	
    xlim([1 length(z)]);
	set(gca, 'Color', 'k');
	ax = gca;
	box off;
	set(ax, 'XTick', [], 'YTick', []);
	outerpos = ax.OuterPosition;
	ti = ax.TightInset;
	left = outerpos(1) + ti(1);
	bottom = outerpos(2) + ti(2);
	ax_width = outerpos(3) - ti(1) - ti(3);
	ax_height = outerpos(4) - ti(2) - ti(4);
	ax.Position = [left bottom ax_width ax_height];
	hgexport(gcf, imgname, hgexport('factorystyle'), 'Format', 'png');
	reset(gca);
	cla(ax);
end