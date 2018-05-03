function showSpectrogram(x, wlen, hop, nfft, window, fs, imgname, windowmap)
%SHOWSPECTROGRAM Displaying a spectrogram generated using a Short-Time Fourier Transform (STFT).
%
%   Author: Lívia Qian

	figure('visible', 'off');
	x = x(:, 1);
	
	% generate spectrogram
	win = windowmap(window);
	spectrogram(x, feval(win, wlen), hop, nfft, fs, 'yaxis');
	
	% export image
	hgexport(gcf, imgname, hgexport('factorystyle'), 'Format', 'png');
end

