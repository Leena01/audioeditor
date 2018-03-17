function showSpectrogram(x, wlen, hop, nfft, fs, imgname)
	figure('visible', 'off');
	x = x(:, 1);
	spectrogram(x, hamming(wlen), hop, nfft, fs, 'yaxis');
	hgexport(gcf, imgname, hgexport('factorystyle'), 'Format', 'png');
end

