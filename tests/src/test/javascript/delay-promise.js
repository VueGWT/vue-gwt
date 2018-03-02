export const delay = function (delay) {
	return new Promise((resolve) => setTimeout(resolve, delay));
};