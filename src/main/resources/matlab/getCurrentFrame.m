function [current] = getCurrentFrame(player)
%GETCURRENTFRAME	Returns the current sample of the given song.
	current = double(get(player, 'CurrentSample'));
end