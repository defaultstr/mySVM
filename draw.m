function draw(pos, neg, svFilename, num)
%DRAW Summary of this function goes here
%   Detailed explanation goes here
fin = fopen(svFilename,'r');
b = fscanf(fin, '%f', 1);
yi = zeros(num,1);
alpha = zeros(num,1);
sv = zeros(num,2);
for i = 1:num
    yi(i) = fscanf(fin, '%d',1);
    alpha(i:i, 1:1) = fscanf(fin, '%f',1);
    sv(i,1) = fscanf(fin, '%f',1);
    sv(i,2) = fscanf(fin, '%f',1);    
end
plot(pos(:,1), pos(:,2), '+');hold on;
plot(neg(:,1), neg(:,2), '.');hold on;
%f = @(x1,y1,x2,y2) (x1*x2 + y1*y2 + 1)^2;
f = @(x1,y1,x2,y2) exp(-0.01*((x1-x2)^2 +(y1-y2)^2));
ezplot(@(x,y)classify(x,y,f,sv,alpha,yi, b));


