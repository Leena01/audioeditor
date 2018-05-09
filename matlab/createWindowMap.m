function [windowmap] = createWindowMap(keySet)
%CREATEWINDOWMAP	Maps some window functions to their names.
	valueSet = {'triang', 'hann', 'hamming', 'blackman', 'blackmanharris', 'flattopwin'};
	windowmap = containers.Map(keySet, valueSet);
end