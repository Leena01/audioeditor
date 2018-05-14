function [x, fs, total] = openSong(file)
%OPENSONG	Reads the given song.
	[x, fs] = audioread(file);
	total = length(x);
end