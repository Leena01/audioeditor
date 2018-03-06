function relocateSong(player, start, empty)
    stop(player);
	if (empty == 1)
		play(player);
	else
		play(player, start);
	end
end