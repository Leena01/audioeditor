function changeSpeed(player, level)
    stop(player);
    set(player, 'SampleRate', get(player, 'SampleRate') * level);
    % resume(player);
end