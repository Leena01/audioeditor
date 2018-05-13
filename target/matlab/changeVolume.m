function [player] = changeVolume(player, x, level)
%CHANGEVOLUME	Changes the volume of the given song.
%	x: samples
%	level: 1/500, 1/250, 1/100, 1/50, 1/25, 1/10, 1/5, 1, 2, 5
	running = strcmp(get(player, 'Running'), 'on');
    current = get(player, 'CurrentSample');
    fs = get(player, 'SampleRate');
	y = x .* level;
	player = audioplayer(y, fs);
	play(player, current);
	
	if ~running
		pause(player);
	end
end