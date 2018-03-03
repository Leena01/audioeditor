function [player, x, total, fs] = cutSong(player, x, from, to)
    stop(player);
	xlen = size(x);
	if xlen(2) == 2
		x = x(from:to);
	else
		x = x(from:to, :);
	end
	fs = get(player, 'SampleRate');
    player = audioplayer(x, fs);
    total = get(player, 'TotalSamples');
end