function showChromagram(x, wlen, hop, nfft, window, fs, imgname, windowmap)
	figure('visible', 'off');
	xsize = size(x);
	if (xsize(2) == 2)
		x = x(:, 1);
	end
	win = windowmap(window);
	[S, ~, ~] = spectrogram(x, feval(win, wlen), hop, nfft, fs, 'yaxis');
	C = getCMatrix(fs, wlen, 27.5);
	Y = C * abs(S);
	imagesc(Y);
	hgexport(gcf, imgname, hgexport('factorystyle'), 'Format', 'png');
end