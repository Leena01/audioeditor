function mindis = calcMinDistance(bpm, fs, lower, smallest)
%CALCMINDISTANCE Calculates the minimum distance between notes measured in
%frames.
%   bpm: BPM
%   fs: Sampling rate
%   lower: Lower value of the time signature (value of beat)
%   smallest: Smallest note value that might appear in the song
    timeOfBeat = 60/bpm;
    if lower ~= smallest
        timeOfBeat = timeOfBeat/(smallest/lower);
    end
    mindis = timeOfBeat * fs * 0.7;
end