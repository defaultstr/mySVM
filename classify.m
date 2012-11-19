function z = classify(x,y,f,sv,a,yi,b )
%CLASSIFY Summary of this function goes here
%   Detailed explanation goes here
svsize = size(sv);
len = svsize(1);
items = zeros(len,1);
for i = 1:len
   items(i) = f(x,y,sv(i,1),sv(i,2));
end
z = sum(yi.*a.*items) + b;
end

