function showChromagram(x, wlen, hop, nfft, window, fs, imgname, windowmap)
%SHOWCHROMAGRAM   Displaying a chromagram generated using a Short-Time Fourier Transform (STFT).
	figure('visible', 'off');
	x = x(:, 1);
	
	% generate chromagram
	win = windowmap(window);
	[S, ~, T] = spectrogram(x, feval(win, wlen), hop, nfft, fs, 'yaxis');
	C = getCMatrix(fs, wlen, 27.5);
	Y = C * abs(S);
	
	% set axis tick labels
	notes = {'G^#/A^b', 'G', 'F^#/G^b', 'F', 'E', 'D^#/E^b', 'D', 'C^#/D^b', 'C', 'B', 'A^#/B^b', 'A'};
	set(gca, 'YTickLabel', notes);
	[T, ~, ut] = engunits(T, 'unicode', 'time');
	
	% plot image
	imagesc(T, 1:12, Y);
	
	% set axis labels
	timelbl = [getString(message('signal:spectrogram:Time')) ' (' ut ')'];
	xlabel(timelbl);
	ylabel('Notes');
	
	% export image
	hgexport(gcf, imgname, hgexport('factorystyle'), 'Format', 'png');
end