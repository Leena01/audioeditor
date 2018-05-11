function showSpectrogram3d(x, wlen, hop, nfft, window, fs, imgname, windowmap)
%SHOWSPECTROGRAM3D Displaying a 3D spectrogram generated using a Short-Time Fourier Transform (STFT).
%	x: samples
%	wlen: window size
%	nfft: number of FFT points
%	window: window function
%	fs: sample rate
%	imgname: name of file to save the image to
%	windowmap: map of window names and window functions
%
%   Author: Lívia Qian

	figure('visible', 'off');
	x = x(:, 1);
	
	% generate spectrogram
	win = windowmap(window);
	spectrogram(x, feval(win, wlen), hop, nfft, fs, 'yaxis');
	view(-45, 45);
	
	% export image
	hgexport(gcf, imgname, hgexport('factorystyle'), 'Format', 'png');
	reset(gca);
end

