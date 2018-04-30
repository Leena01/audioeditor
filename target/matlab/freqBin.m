function [bin] = freqBin(f, fs, nfft)
% f in [fs/nfft * bin, fs/nfft * (bin + 1)]
    bin = floor(f * nfft / fs) + 1;
end

