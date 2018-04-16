function [status] = isPlaying(player)
	status = strcmp(get(player, 'Running'), 'on');
end