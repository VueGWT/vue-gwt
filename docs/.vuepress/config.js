module.exports = {
  title: 'Vue GWT',
  description: 'Vue Components written in Java',
  base: "/vue-gwt/",
  themeConfig: {
    repo: 'axellience/vue-gwt',
    docsDir: 'docs',
    editLinks: true,
    editLinkText: 'Help us improve this page!',
    algolia: {
      apiKey: '808fd9f88b8a6b9fe8cc8ceb619a2b0b',
      indexName: 'vue_gwt'
    },
    nav: [
      {text: 'Home', link: '/'},
      {text: 'Guide', link: '/guide/'}
    ],
    sidebarDepth: 2,
    sidebar: {
      '/guide/': [
        {
          collapsable: false,
          children: [
            '',
            'project-setup',
            'getting-started',
          ]
        },
        {
          title: 'Essentials',
          collapsable: false,
          children: [
            'essentials/the-vue-instance',
            'essentials/reactivity-system',
            'essentials/template-syntax',
            'essentials/computed-and-watchers',
            'essentials/class-and-style',
            'essentials/conditional',
            'essentials/list',
            'essentials/events',
            'essentials/forms',
            'essentials/components',
            'essentials/dependency-injection',
          ]
        },
        {
          title: 'Transitions & Animation',
          collapsable: false,
          children: [
            'transitions/transitions',
            'transitions/transitioning-state',
          ]
        },
        {
          title: 'Reusability & Composition',
          collapsable: false,
          children: [
            'composition/extending-components',
            'composition/custom-directives',
            'composition/render-function',
            'composition/plugins',
          ]
        },
        {
          title: 'Integration with GWT',
          collapsable: false,
          children: [
            'gwt-integration/client-bundles-and-styles',
            'gwt-integration/widgets',
            'gwt-integration/js-interop',
          ]
        },
        {
          title: 'Tooling',
          collapsable: false,
          children: [
            'tooling/unit-testing'
          ]
        },
        {
          title: 'Scaling Up',
          collapsable: false,
          children: [
            'scaling-up/routing'
          ]
        },
        {
          title: 'Advanced Topics',
          collapsable: false,
          children: [
            'advanced/custom-elements',
            'advanced/integrating-with-js-components',
            'advanced/unsupported-features',
          ]
        }
      ]
    }
  }
};