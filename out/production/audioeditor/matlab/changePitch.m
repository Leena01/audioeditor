function [player] = changePitch(player, level)
    set(player, 'SampleRate', get(player, 'SampleRate') * level);
end