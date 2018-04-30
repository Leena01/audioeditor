function showSpectrogram(x, wlen, hop, nfft, window, fs, imgname, windowmap)
	figure('visible', 'off');
	xsize = size(x);
	if (xsize(2) == 2)
		x = x(:, 1);
	end
	win = windowmap(window);
	spectrogram(x, feval(win, wlen), hop, nfft, fs, 'yaxis');
	hgexport(gcf, imgname, hgexport('factorystyle'), 'Format', 'png');
end

