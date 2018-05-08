function [bin] = freqBin(f, fs, nfft)
% FREQBIN	Determines the bin the given frequency falls into.
%	f: frequency
%	sampling rate
%	nfft: Number of FFT points
% f in [fs/nfft * bin, fs/nfft * (bin + 1)]
    bin = floor(f * nfft / fs) + 1;
end

