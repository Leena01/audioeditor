function showSpectrogram(x, wlen, hop, nfft, window, fs, imgname, windowmap)
	figure('visible', 'off');
	x = x(:, 1);
	win = windowmap(window);
	spectrogram(x, feval(win, wlen), hop, nfft, fs, 'yaxis');
	hgexport(gcf, imgname, hgexport('factorystyle'), 'Format', 'png');
end

