function output = beatAlgo(x, minbpm, maxbpm)
%BEATALGO Finds the tempo of a musical signal, given the minimum
% and the maximum.
    a = filterbank(x);
    b = hwindow(a);
    c = diffrect(b);
    output = timecomb(c, 2, minbpm, maxbpm);
end