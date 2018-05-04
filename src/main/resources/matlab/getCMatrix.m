function [C] = getCMatrix(Fs, N, A0)
%GETCMATRIX	Gets the matrix that amplifies the frequency of each note.
%	Fs: Sampling rate of audio (cycles/second)
%	N: The window size of the short-time Fourier Transform
    NSpec = N/2 + 1; %The number of bins in the spectrogram
    C = zeros(12, NSpec); %Allocate space for the C matrix
%   A0 = 440.0/4; %The lowest octave range to search
    FMax = Fs/2; %Maximum frequency supported by this sampling rate
     
    for i = 1:12
        row = zeros(1, NSpec); %This row of the C matrix
        fBase = A0 * 2^((i - 1)/12); %The base frequency for this note, in relation to A0
        maxOctave = floor(log(FMax/fBase)/log(2)); %The maximum octave index
        for octave = 0:maxOctave
            f = fBase*2^octave;
            k = freqBin(f, Fs, N); %Find the fractional bin index "k" of the frequency corresponding
            %to this halfstep and this octave
             
            %Create an exponential gaussian bump around k in the frequency
            %domain
            bump = 0:NSpec-1;
            bump = exp(-20*abs(log(bump/k)/log(2)));
            bump = bump/norm(bump);
            row = row + bump;
        end
        C(13 - i, :) = row;
    end
%     imagesc(C);
%     pause;
end