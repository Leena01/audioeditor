function showSpectrogram3d(x, wlen, hop, nfft, window, fs, imgname, windowmap)
	figure('visible', 'off');
	x = x(:, 1);
	win = windowmap(window);
	spectrogram(x, feval(win, wlen), hop, nfft, fs, 'yaxis');
	org.ql.audioeditor.org.ql.audioeditor.view(-45, 45);
	hgexport(gcf, imgname, hgexport('factorystyle'), 'Format', 'png');
	reset(gca);
end

