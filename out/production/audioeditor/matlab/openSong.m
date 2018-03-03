function [player, x, total, fs] = openSong(path)
	[x, fs] = audioread(path);
	player = audioplayer(x, fs);
	total = get(player, 'TotalSamples');
end