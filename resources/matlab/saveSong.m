function saveSong(file, x, fs)
	[filepath, name, ext] = fileparts(file);
	if (strcmp(ext, '.mp3'))
		mp3write(x, fs, file);
	else
		audiowrite(path, x, fs);
	end
end