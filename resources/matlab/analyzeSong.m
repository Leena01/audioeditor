function analyzeSong(x, wlen, hop, nfft, fs, channel)
    x1 = x(:, 1);
    x2 = x(:, 2);
    if (channel == 2)
        [S, F, T] = stft(x1, wlen, hop, nfft, fs);
    else
        [S, F, T] = stft(x2, wlen, hop, nfft, fs);
    end
	imagesc(T, F, log10(abs(S)));
	set(gca,'YDir', 'Normal');
	xlabel('Time (secs)');
	ylabel('Freq (Hz)');
	title('Short-time Fourier Transform spectrum');
	expon = get(colorbar, 'YTick');
	colorbar('YTickLabel', 10.^abs(expon));
end

