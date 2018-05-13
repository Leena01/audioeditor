function [status] = isPlaying(player)
%ISPLAYING	Returns whether the current song is playing or not.
	status = strcmp(get(player, 'Running'), 'on');
end