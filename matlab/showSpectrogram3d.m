function showSpectrogram3d(x, wlen, hop, nfft, window, fs, imgname, windowmap)
%SHOWSPECTROGRAM3D Displaying a 3D spectrogram generated using a Short-Time Fourier Transform (STFT).
%
%   Author: Lívia Qian

	figure('visible', 'off');
	x = x(:, 1);
	win = windowmap(window);
	spectrogram(x, feval(win, wlen), hop, nfft, fs, 'yaxis');
	view(-45, 45);
	hgexport(gcf, imgname, hgexport('factorystyle'), 'Format', 'png');
	reset(gca);
end

