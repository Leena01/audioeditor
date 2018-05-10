function [note, scale] = getKey(A)
	maj = zeros(12, 1);
	min = zeros(12, 1);
	major = [1, 5, 8];
	minor = [1, 4, 8];
	for i = 1:12
		fst = mod((major(1) + i - 1), 12);
		snd = mod((major(2) + i - 1), 12);
		trd = mod((major(3) + i - 1), 12);
		if  fst == 0 fst = 12; end
		if  snd == 0 snd = 12; end
		if  trd == 0 trd = 12; end
		maj(i, 1) = A(fst) + A(snd) + A(trd);

		fst = mod((minor(1) + i - 1), 12);
		snd = mod((minor(2) + i - 1), 12);
		trd = mod((minor(3) + i - 1), 12);
		if  fst == 0 fst = 12; end
		if  snd == 0 snd = 12; end
		if  trd == 0 trd = 12; end
		min(i, 1) = A(fst) + A(snd) + A(trd);
	end
	
	no = {'A', 'B-flat', 'B', 'C', 'D-flat', 'D', 'E-flat', 'E', 'F', 'G-flat', 'G', 'A-flat'};
	
	[maxmajor, majorind] = max(maj);
	[maxminor, minorind] = max(min);
	if maxmajor > maxminor
		note = no(majorind);
		scale = 'major';
	else
		note = no(minorind);
		scale = 'minor';
	end
end