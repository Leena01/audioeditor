function plotSong(x, fs, imgname)	
	figure('visible', 'off');
	xsize = size(x);
	xlen = length(x);
	tlen = xlen/fs; tper = 1/fs;
	t = 0:tper:(xlen/fs) - tper;

	if (xsize(2) == 2)
		x1 = x(:, 1);
		x2 = x(:, 2);
		plot(t, x1, 'b', t, x2);
	else
		plot(t, x);
	end

	set(gca, 'Color', 'k');
	axis([t(1) t(end) -inf inf]);
	ax = gca;
	box off;
	set(ax, 'XTick', [], 'YTick', []);
	outerpos = ax.OuterPosition;
	ti = ax.TightInset;
	left = outerpos(1) + ti(1);
	bottom = outerpos(2) + ti(2);
	ax_width = outerpos(3) - ti(1) - ti(3);
	ax_height = outerpos(4) - ti(2) - ti(4);
	ax.Position = [left bottom ax_width ax_height];
	hgexport(gcf, imgname, hgexport('factorystyle'), 'Format', 'png');
	reset(gca);
	cla(ax);
end