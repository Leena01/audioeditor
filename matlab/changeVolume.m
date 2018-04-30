function [player] = changeVolume(player, x, fs, level)
% level: 1/500, 1/250, 1/100, 1/50, 1/25, 1/10, 1/5, 1, 2, 5
	running = strcmp(get(player, 'Running'), 'on');
    current = get(player, 'CurrentSample');
	y = x .* level;
	player = audioplayer(y, fs);
	play(player, current);
	
	if (~running)
		pause(player);
	end
end