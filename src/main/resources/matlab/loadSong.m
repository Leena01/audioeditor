function [player] = loadSong(x, fs)
%LOADSONG	Loads the given song. Return a handle.
% x: samples
% fs: sample rate
	player = audioplayer(x, fs);
end
