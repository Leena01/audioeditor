function B = minkcol(A, k)
%MAXCOL	Returns a matrix containing the k smallest elements of a each column in a matrix.
%	A: input matrix
%	k: number of elements
    [~, b] = size(A);
    B = zeros(k, b);
    for i = 1:b
        [~, ind] = sort(A(:, i));
        B(:, i) = ind(1:k);
    end
end