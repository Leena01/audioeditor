function sim = noteDetection(S1, locs1, n, wlen, nfft, hop)
    %note = file;
    [x2, fs2] = audioread(n);
    x2 = x2(:, 1);
    
    % Short-time Fourier transform
    [S2, F2, T2] = stft(x2, wlen, hop, nfft, fs2);
    [sheight, slen] = size(S1);
    [~, slen2] = size(S2);
    
    % Peaks
    locs2 = onsets(x2, fs2, 120, 10, 2, 2);
    locs2 = int32(locs2*(slen2/length(x2)));
    llen = length(locs1);
    
    % Cut control sound
    S2 = S2./max(max(S2));
    S2 = S2(:, locs2(1):slen2);
    % New length
    [~, slen2] = size(S2);
    
    sim = zeros(1, llen);
    for i=1:llen
        % Calculate the start end end point
        beginning = locs1(i);
        if i == llen
            ending = slen;
        else
            ending = locs1(i + 1) - 1;
        end
        % Length of note
        seglen = ending - beginning + 1;
        
        % Extract note
        note = S1(:, beginning:ending);
        
        % Set the size of the note
        if slen2 >= seglen
            note2 = S2(:, 1:seglen);
        else
            note2 = S2;
            note2(sheight, seglen) = 0;
        end
        
        % Similarity
        sim(i) = norm(note - note2);
    end
end