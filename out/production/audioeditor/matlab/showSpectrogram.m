function showSpectrogram(x, wlen, hop, nfft, window, fs, imgname)
	figure('visible', 'off');
	
	keySet = {0, 1, 2, 3, 4, 5};
	valueSet = {'triang', 'hann', 'hamming', 'blackman', 'blackmanharris', 'flattopwin'};
	M = containers.Map(keySet, valueSet);

	x = x(:, 1);
	win = str2func(M(window));
	spectrogram(x, win(wlen), hop, nfft, fs, 'yaxis');
	hgexport(gcf, imgname, hgexport('factorystyle'), 'Format', 'png');
end

