function showSpectrogram3d(x, wlen, hop, nfft, fs, imgname)
	figure('visible', 'off');
	x = x(:, 1);
	spectrogram(x, hamming(wlen), hop, nfft, fs, 'yaxis');
	view(-45, 45);
	hgexport(gcf, imgname, hgexport('factorystyle'), 'Format', 'png');
	reset(gca);
end

