function saveSong(file, x, fs)
%SAVESONG	Saves the given song.
%	file: filename
%	x: samples
%	fs: sample rate
% All platforms: WAVE (.wav), OGG (.ogg), FLAC (.flac)
% Windows® and Mac: MPEG-4 AAC (.m4a, .mp4)
	audiowrite(file, x, fs);
end