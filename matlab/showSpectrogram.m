function showSpectrogram(x, wlen, noverlap, nfft, window, fs, imgname, windowmap)
%SHOWSPECTROGRAM Displaying a spectrogram generated using a Short-Time Fourier Transform (STFT).
%	x: samples
%	wlen: window size
%	noverlap: number of overlapped samples
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
	spectrogram(x, feval(win, wlen), noverlap, nfft, fs, 'yaxis');
	
	% export image
	hgexport(gcf, imgname, hgexport('factorystyle'), 'Format', 'png');
end

