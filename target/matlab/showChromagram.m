function showChromagram(x, wlen, noverlap, nfft, window, fs, imgname, windowmap)
%SHOWCHROMAGRAM   Displaying a chromagram generated using a Short-Time Fourier Transform (STFT).
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
	
	% generate chromagram
	win = windowmap(window);
	[S, ~, T] = spectrogram(x, feval(win, wlen), noverlap, nfft, fs, 'yaxis');
	C = getCMatrix(fs, nfft, 27.5);
	Y = C * abs(S);
	
	% plot image
	imagesc(T, 1:12, Y);
	% set axis tick labels
	notes = {'G^#/A^b', 'G', 'F^#/G^b', 'F', 'E', 'D^#/E^b', 'D', 'C^#/D^b', 'C', 'B', 'A^#/B^b', 'A'};
	set(gca, 'YTick', 1:12);
    set(gca, 'YTickLabel', notes);
	[T, ~, ut] = engunits(T, 'unicode', 'time');
	% set axis labels
	timelbl = [getString(message('signal:spectrogram:Time')) ' (' ut ')'];
	xlabel(timelbl);
	ylabel('Notes');
	
	% export image
	hgexport(gcf, imgname, hgexport('factorystyle'), 'Format', 'png');
end