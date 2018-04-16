function relocateSong(player, start, empty)
	running = strcmp(get(player, 'Running'), 'on');
    stop(player);
	if (empty == 1)
		play(player);
	else
		play(player, start);
	end
	
	if (~running)
		pause(player);
	end
end