const gwtReadyPromise = new Promise((onSuccess) => {
  if (!window.onVueGwtTestsReady) {
    window.onVueGwtTestsReady = [];
  }
  window.onVueGwtTestsReady.push(onSuccess);
});

export const onGwtReady = () => gwtReadyPromise;

export const createAndMountComponent = qualifiedName => {
  const div = document.createElement("div");
  document.body.appendChild(div);
  const ComponentConstructor = window.vueGwtTestComponents[qualifiedName];
  return new ComponentConstructor({el: div});
};

export const destroyComponent = component => {
  component.$destroy();
  document.body.removeChild(component.$el);
};

export const nextTick = () => Vue.nextTick();

export const getElement = (component, query) => component.$el.querySelector(
    query);

export const triggerEvent = (target, event, process) => {
  const e = document.createEvent('HTMLEvents');
  e.initEvent(event, true, true);
  if (process) {
    process(e);
  }
  target.dispatchEvent(e);
};