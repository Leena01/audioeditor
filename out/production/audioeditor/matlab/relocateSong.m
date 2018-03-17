function relocateSong(player, start, empty, isplaying)
    stop(player);
	if (empty == 1)
		play(player);
	else
		play(player, start);
	end
	
	if (isplaying == 0)
		pause(player);
	end
end