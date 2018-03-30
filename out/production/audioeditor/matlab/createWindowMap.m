function [windowmap] = createWindowMap(keySet)
	valueSet = {'triang', 'hann', 'hamming', 'blackman', 'blackmanharris', 'flattopwin'};
	windowmap = containers.Map(keySet, valueSet);
end