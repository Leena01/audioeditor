function [current] = getCurrentFrame(player)
	current = double(get(player, 'CurrentSample'));
end