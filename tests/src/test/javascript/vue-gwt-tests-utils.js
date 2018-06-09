import {expect} from "chai";

const gwtReadyPromise = new Promise((onSuccess) => {
  if (!window.onVueGwtTestsReady) {
    window.onVueGwtTestsReady = [];
  }
  window.onVueGwtTestsReady.push(onSuccess);
});

export const onGwtReady = function () {
  return gwtReadyPromise;
};

export const createAndMountComponent = function (qualifiedName) {
  const div = document.createElement("div");
  document.body.appendChild(div);
  const ComponentConstructor = window.VueGWT.getJsConstructor(qualifiedName);
  return new ComponentConstructor({el: div});
};

export const destroyComponent = function (component) {
  component.$destroy();
  document.body.removeChild(component.$el);
};

export const onNextTick = function (testFunction) {
  return new Promise((resolve, reject) => {
    Vue.nextTick(() => {
      try {
        testFunction();
        resolve();
      } catch (e) {
        reject(e);
      }
    });
  });
};

export const getElement = function (component, query) {
  return component.$el.querySelector(query);
};