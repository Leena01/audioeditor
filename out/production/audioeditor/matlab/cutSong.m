function [x] = cutSong(x, from, to)
	xlen = size(x);
	if xlen(2) == 2
		x = x(from:to);
	else
		x = x(from:to, :);
	end
end