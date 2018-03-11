function analyzeSong(x, wlen, hop, nfft, fs, imgname)
	figure('visible', 'off');
	[S, F, T] = stft(x, wlen, hop, nfft, fs);
	imagesc(T, F, log10(abs(S)));
	set(gca, 'YDir', 'Normal');
	xlabel('Time (secs)');
	ylabel('Freq (Hz)');
	title('Short-time Fourier Transform spectrum');
	expon = get(colorbar, 'YTick');
	colorbar('YTickLabel', 10.^abs(expon));
	hgexport(gcf, imgname, hgexport('factorystyle'), 'Format', 'png');
	reset(gca);
end

