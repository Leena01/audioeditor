function [player, x, total, fs] = openSong(file)
	[x, fs] = audioread(file);
	player = audioplayer(x, fs);
	total = get(player, 'TotalSamples');
end