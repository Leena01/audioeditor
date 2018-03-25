function saveSong(file, x, fs)
% All platforms: WAVE (.wav), OGG (.ogg), FLAC (.flac)
% Windows® and Mac: MPEG-4 AAC (.m4a, .mp4)
	[filepath, name, ext] = fileparts(file);
	if (strcmp(ext, '.mp3'))
		mp3write(x, fs, file);
	else
		audiowrite(path, x, fs);
	end
end