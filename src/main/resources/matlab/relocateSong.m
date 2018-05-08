function relocateSong(player, start, empty)
%RELOCATESONG	Sets the current song to a new position.
%	player: song handle
%	start: starting position
%	empty: empty if start is not a valid position
	running = strcmp(get(player, 'Running'), 'on');
    stop(player);
	if empty == 1
		play(player);
	else
		play(player, start);
	end
	
	if ~running
		pause(player);
	end
end