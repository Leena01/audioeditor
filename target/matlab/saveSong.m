function saveSong(file, x, fs)
% All platforms: WAVE (.wav), OGG (.ogg), FLAC (.flac)
% Windows® and Mac: MPEG-4 AAC (.m4a, .mp4)
	audiowrite(file, x, fs);
end