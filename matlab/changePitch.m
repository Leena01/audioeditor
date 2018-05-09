function [player] = changePitch(player, fs)
%CHANGEPITCH	Changes the pitch of the given song.
    set(player, 'SampleRate', fs);
end